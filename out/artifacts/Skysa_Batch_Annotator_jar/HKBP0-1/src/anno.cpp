#include <stdio.h>
#include "stdio__iob_func.h"

#include <iostream> // cerr
#include <fstream> // ifstream
#include <vector>
#include <string>

#include <Common/Base/keycode.cxx> 
#include <Common/Base/Config/hkProductFeatures.cxx>

#include <Common/Base/Memory/System/hkMemorySystem.h>
#include <Common/Base/Memory/System/Util/hkMemoryInitUtil.h>
#include <Common/Base/Memory/Allocator/Malloc/hkMallocAllocator.h>

#include <Common/Base/System/Io/IStream/hkIStream.h>
#include <Common/Base/System/Io/Reader/hkStreamReader.h>
#include <Common/Base/System/Io/OStream/hkOStream.h>
#include <Common/Base/System/Io/Writer/hkStreamWriter.h>

//#include <Common/Base/Reflection/Registry/hkDynamicClassNameRegistry.h>
#include <Common/Base/Reflection/Registry/hkDefaultClassNameRegistry.h>

// Serialize Loader
#include <Common/Serialize/Util/hkSerializeUtil.h>
#include <Common/Serialize/Util/hkRootLevelContainer.h>
#include <Common/Serialize/Util/hkNativePackfileUtils.h>
#include <Common/Serialize/Util/hkLoader.h>
#include <Common/Serialize/Version/hkVersionPatchManager.h>
#include <Common/Compat/Deprecated/Packfile/Binary/hkBinaryPackfileReader.h>
#include <Common/Compat/Deprecated/Packfile/Xml/hkXmlPackfileReader.h>

// Animation
#include <Animation/Animation/hkaAnimationContainer.h>
#include <Animation/Animation/Rig/hkaPose.h>
#include <Animation/Animation/Rig/hkaSkeleton.h>
#include <Animation/Animation/Rig/hkaSkeletonUtils.h>

#include "HctFilterManager.h"

#pragma comment (lib, "hkBase.lib")
#pragma comment (lib, "hkSerialize.lib")
#pragma comment (lib, "hkSceneData.lib")
#pragma comment (lib, "hkInternal.lib")
#pragma comment (lib, "hkGeometryUtilities.lib")
//o1: #pragma comment (lib, "hkVisualize.lib")
#pragma comment (lib, "hkCompat.lib")
#pragma comment (lib, "hkpCollide.lib")
#pragma comment (lib, "hkpConstraintSolver.lib")
#pragma comment (lib, "hkpDynamics.lib")
#pragma comment (lib, "hkpInternal.lib")
#pragma comment (lib, "hkpUtilities.lib")
#pragma comment (lib, "hkpVehicle.lib")
#pragma comment (lib, "hkaAnimation.lib")
#pragma comment (lib, "hkaRagdoll.lib")
#pragma comment (lib, "hkaInternal.lib")
//o1: #pragma comment (lib, "hkgBridge.lib")

void HK_CALL errorReport(const char* msg, void* userContext)
{
	std::cerr << msg << std::endl;
}

hkResource* hkSerializeUtilLoad( hkStreamReader* stream
		, hkSerializeUtil::ErrorDetails* detailsOut/*=HK_NULL*/
		, const hkClassNameRegistry* classReg/*=HK_NULL*/
		, hkSerializeUtil::LoadOptions options/*=hkSerializeUtil::LOAD_DEFAULT*/ )
{
   __try
   {
	  return hkSerializeUtil::load(stream, detailsOut, classReg, options);
   }
   __except (EXCEPTION_EXECUTE_HANDLER)
   {
	  if (detailsOut == NULL)
		 detailsOut->id = hkSerializeUtil::ErrorDetails::ERRORID_LOAD_FAILED;
	  return NULL;
   }
}

hkResult hkSerializeLoad(hkStreamReader *reader
		, hkVariant &root
		, hkResource *&resource)
{
   hkTypeInfoRegistry &defaultTypeRegistry = hkTypeInfoRegistry::getInstance();
   hkDefaultClassNameRegistry &defaultRegistry = hkDefaultClassNameRegistry::getInstance();

   hkBinaryPackfileReader bpkreader;
   hkXmlPackfileReader xpkreader;
   resource = NULL;
   hkSerializeUtil::FormatDetails formatDetails;
   hkSerializeUtil::detectFormat( reader, formatDetails );
   hkBool32 isLoadable = hkSerializeUtil::isLoadable( reader );
   if (!isLoadable && formatDetails.m_formatType != hkSerializeUtil::FORMAT_TAGFILE_XML)
   {
	  return HK_FAILURE;
   }
   else
   {
	  switch ( formatDetails.m_formatType )
	  {
	  case hkSerializeUtil::FORMAT_PACKFILE_BINARY:
		 {
			bpkreader.loadEntireFile(reader);
			bpkreader.finishLoadedObjects(defaultTypeRegistry);
			if ( hkPackfileData* pkdata = bpkreader.getPackfileData() )
			{
			   hkArray<hkVariant>& obj = bpkreader.getLoadedObjects();
			   for ( int i =0,n=obj.getSize(); i<n; ++i)
			   {
				  hkVariant& value = obj[i];
				  if ( value.m_class->hasVtable() )
					 defaultTypeRegistry.finishLoadedObject(value.m_object, value.m_class->getName());
			   }
			   resource = pkdata;
			   resource->addReference();
			}
			root = bpkreader.getTopLevelObject();
		 }
		 break;

	  case hkSerializeUtil::FORMAT_PACKFILE_XML:
		 {
			xpkreader.loadEntireFileWithRegistry(reader, &defaultRegistry);
			if ( hkPackfileData* pkdata = xpkreader.getPackfileData() )
			{
			   hkArray<hkVariant>& obj = xpkreader.getLoadedObjects();
			   for ( int i =0,n=obj.getSize(); i<n; ++i)
			   {
				  hkVariant& value = obj[i];
				  if ( value.m_class->hasVtable() )
					 defaultTypeRegistry.finishLoadedObject(value.m_object, value.m_class->getName());
			   }
			   resource = pkdata;
			   resource->addReference();
			   root = xpkreader.getTopLevelObject();
			}
		 }
		 break;

	  case hkSerializeUtil::FORMAT_TAGFILE_BINARY:
	  case hkSerializeUtil::FORMAT_TAGFILE_XML:
	  default:
		 {
			hkSerializeUtil::ErrorDetails detailsOut;
			hkSerializeUtil::LoadOptions loadflags = hkSerializeUtil::LOAD_FAIL_IF_VERSIONING;
			resource = hkSerializeUtilLoad(reader, &detailsOut, &defaultRegistry, loadflags);
			root.m_object = resource->getContents<hkRootLevelContainer>();
			if (root.m_object != NULL)
			   root.m_class = &((hkRootLevelContainer*)root.m_object)->staticClass();
		 }
		 break;
	  }
   }
   return root.m_object != NULL ? HK_SUCCESS : HK_FAILURE;
}

hkResource *hkSerializeLoadResource(hkStreamReader *reader)
{
   hkResource *resource = NULL;
   hkVariant root;   
   hkSerializeLoad(reader, root, resource);
   return resource;
}

hkResult hkSerializeUtilSave( hkVariant& root, hkOstream& stream )
{
	hkPackfileWriter::Options packFileOptions;
	packFileOptions.m_layout = hkStructureLayout::MsvcAmd64LayoutRules;

	hkResult res;
	__try
	{
		res = hkSerializeUtil::savePackfile( root.m_object, *root.m_class, stream.getStreamWriter(), packFileOptions );
	}
	__except (EXCEPTION_EXECUTE_HANDLER)
	{
		res = HK_FAILURE;
	}
	if ( res != HK_SUCCESS )
	{
		std::cerr << "Havok reports save failed.";
	}
	return res;
}

void write(hkOstream& o, hkaAnimation *anim)
{
		/// Returns the number of original samples / frames of animation.
	int numOriginalFrames = anim->getNumOriginalFrames();
		/// The length of the animation cycle in seconds
	hkReal duration = anim->m_duration;

	o.printf("# numOriginalFrames: %d\r\n", numOriginalFrames);
	o.printf("# duration: %.6f\r\n", duration);

			/// The annotation tracks associated with this skeletal animation.
	const int numAnnotationTracks = anim->m_annotationTracks.getSize();
	o.printf("# numAnnotationTracks: %d\r\n", numAnnotationTracks);

	hkArray< class hkaAnnotationTrack >::const_iterator annotationTrack = anim->m_annotationTracks.begin();
	if (annotationTrack != anim->m_annotationTracks.end())
	{
		const int numAnnotations = annotationTrack->m_annotations.getSize();
		o.printf("# numAnnotations: %d\r\n", numAnnotations);

		for (hkArray< class hkaAnnotationTrack::Annotation >::const_iterator annotation = annotationTrack->m_annotations.begin(); annotation != annotationTrack->m_annotations.end(); ++annotation)
		{
			o.printf("%.6f", annotation->m_time);
			o << " " << annotation->m_text << "\r\n";
		}
	}
}

void dump(const char* filename, const char* annoname)
{
	hkOstream o(annoname);

	hkIstream stream(filename);
	hkStreamReader *reader = stream.getStreamReader();
	hkResource *resource = hkSerializeLoadResource(reader);
	if (resource == NULL)
	{
		std::cerr << "File is not loadable" << std::endl;
	}
	else
	{
		if (hkRootLevelContainer* rootCont = resource->getContents<hkRootLevelContainer>())
		{
			if (hkaAnimationContainer *animCont = rootCont->findObject<hkaAnimationContainer>())
			{
				int nanimations = animCont->m_animations.getSize();
				if (nanimations != 0)
				{
					hkaAnimation *animation = animCont->m_animations[0];
					write(o, animation);
				}
			}
		}
		resource->removeReference();
	}
}

std::string trim(const std::string& string, const char* trimCharacterList = " \t\v\r\n")
{
	std::string result;
	 
	std::string::size_type left = string.find_first_not_of(trimCharacterList);
	 
	if (left != std::string::npos)
	{
		std::string::size_type right = string.find_last_not_of(trimCharacterList);
		 
		result = string.substr(left, right - left + 1);
	}
	 
	return result;
}

struct RawAnnotation
{
	std::string m_time;
	std::string m_text;
};

std::vector<RawAnnotation> m_annotations;

bool load_annotations(const char* annoname)
{
	std::ifstream fromFile(annoname);
	if (!fromFile)
	{
		//_DMESSAGE("error on i/o operation");
		return false;
	}

	std::string line;
	while (std::getline(fromFile, line))
	{
		line = trim(line);
		if (line[0] == '#') //comment line
			continue;
		std::string::size_type del_pos = line.find_first_of(' ');
		std::string::size_type comment_pos = line.find_first_of('#');
		if (del_pos == std::string::npos)
			continue;
		std::string time = line.substr(0, del_pos);

		std::string::size_type text_pos = del_pos+1;
		std::string::size_type text_len = std::string::npos;
		if (comment_pos != std::string::npos)
			text_len = comment_pos - text_pos;

		std::string text = line.substr(text_pos, text_len);
		time = trim(time);
		text = trim(text);

		RawAnnotation annotation;
		annotation.m_time = time;
		annotation.m_text = text;
		m_annotations.push_back(annotation);
	}
	return true;
}

void update_annotations(hkaAnimation *anim)
{
	hkArray< class hkaAnnotationTrack >::iterator annotationTrack = anim->m_annotationTracks.begin();
	annotationTrack->m_annotations.setSize(m_annotations.size());
	int i=0;
	for (hkArray< class hkaAnnotationTrack::Annotation >::iterator annotation = annotationTrack->m_annotations.begin(); annotation != annotationTrack->m_annotations.end(); ++annotation, ++i)
	{
		annotation->m_time = strtod(m_annotations[i].m_time.c_str(), NULL);
		annotation->m_text = m_annotations[i].m_text.c_str();
	}
}

void update(const char* filename, const char* annoname, const char* destname)
{
	if (!load_annotations(annoname))
		return;

	hkIstream stream(filename);
	hkStreamReader *reader = stream.getStreamReader();
	hkResource *resource = hkSerializeLoadResource(reader);
	if (resource == NULL)
	{
		std::cerr << "File is not loadable" << std::endl;
	}
	else
	{
		if (hkRootLevelContainer* rootCont = resource->getContents<hkRootLevelContainer>())
		{
			if (hkaAnimationContainer *animCont = rootCont->findObject<hkaAnimationContainer>())
			{
				int nanimations = animCont->m_animations.getSize();
				if (nanimations != 0)
				{
					hkaAnimation *animation = animCont->m_animations[0];
					update_annotations(animation);

					// save src.hkx
					hkVariant root = { rootCont, &rootCont->staticClass() };
					hkOstream deststream(destname);
					hkSerializeUtilSave(root, deststream);
				}
			}
		}
		resource->removeReference();
	}
}

void cout_usage()
{
	std::cout << "Usage: hkanno64.exe command options filename" << std::endl;
	std::cout << "  dump -o anno.txt anim.hkx" << std::endl;
	std::cout << "  update -i anno.txt anim.hkx" << std::endl;
}

int main( int argc, char *argv[], char *envp[] )
{
	char *filename = NULL;
	char *ofilename = NULL;
	char *ifilename = NULL;

	if (argc < 2)
	{
		cout_usage();
		return 1;
	}

	char *cmd = argv[1];

	for (int i=2; i<argc; ++i)
	{
		if (argv[i][0] == '-')
		{
			switch (argv[i][1])
			{
				case '?':
					break;
				case 'i':
					if (++i<argc)
					{
						ifilename = argv[i];
					}
					break;
				case 'o':
					if (++i<argc)
					{
						ofilename = argv[i];
					}
					break;
			}
		}
		else
		{
			filename = argv[i];
		}
	}

	if (filename == NULL)
	{
		cout_usage();
		return 1;
	}

	// Need to have memory allocated for the solver. Allocate 1mb for it.
	hkMemoryRouter *memoryRouter = hkMemoryInitUtil::initDefault( hkMallocAllocator::m_defaultMallocAllocator, hkMemorySystem::FrameInfo(1024 * 1024) );
	hkBaseSystem::init( memoryRouter, errorReport );

	switch (cmd[0])
	{
		case 'd':
			{
				if (ofilename == NULL)
					ofilename = "anno.txt";

				ExecuteHctFilterManager(filename);

				dump("win32.hkx", ofilename);
			}
			break;
		case 'u':
			{
				if (ifilename == NULL)
					ifilename = "anno.txt";

				ExecuteHctFilterManager(filename);

				update("win32.hkx", ifilename, filename);
			}
			break;
	}


	// Shutdown
	hkBaseSystem::quit();
	hkMemoryInitUtil::quit();

	return 0;
}
