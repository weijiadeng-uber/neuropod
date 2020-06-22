export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home
export MY_JNI_HEADERS=${PWD}/src/main/native
export PROJECT_PACKAGE=${PWD}/src/main/java/org/neuropod
export CPP_FOLDER=${PWD}/src/main/native
export NEUROPOD_PATH = /usr/local/lib/libneuropod.so

export OUT_DIR=${PWD}/out

javaclass:
	mkdir -p ${OUT_DIR}
	javac -h ${MY_JNI_HEADERS} -d ${OUT_DIR} ${PROJECT_PACKAGE}/LibraryLoader.java ${PROJECT_PACKAGE}/ReferenceCounter.java ${PROJECT_PACKAGE}/NeuropodValue.java ${PROJECT_PACKAGE}/NeuropodValueMap.java ${PROJECT_PACKAGE}/NativeClass.java ${PROJECT_PACKAGE}/Neuropod.java ${PROJECT_PACKAGE}/RuntimeOptions.java ${PROJECT_PACKAGE}/DataType.java ${PROJECT_PACKAGE}/NeuropodJNIException.java ${PROJECT_PACKAGE}/TestMain.java

jclass_register.o: javaclass
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -I${MY_JNI_HEADERS} ${CPP_FOLDER}/jclass_register.cc -o ${OUT_DIR}/jclass_register.o -std=c++14

utils.o: javaclass
	mkdir -p ${OUT_DIR}
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -I${MY_JNI_HEADERS} ${CPP_FOLDER}/utils.cc -o ${OUT_DIR}/utils.o -std=c++14

org_neuropod_LibraryLoader.o: javaclass
	mkdir -p ${OUT_DIR}
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -I${MY_JNI_HEADERS} ${CPP_FOLDER}/org_neuropod_LibraryLoader.cc -o ${OUT_DIR}/org_neuropod_LibraryLoader.o -std=c++14

org_neuropod_Neuropod.o: javaclass
	mkdir -p ${OUT_DIR}
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -I${MY_JNI_HEADERS} ${CPP_FOLDER}/org_neuropod_Neuropod.cc -o ${OUT_DIR}/org_neuropod_Neuropod.o -std=c++14

org_neuropod_NeuropodValue.o: javaclass
	mkdir -p ${OUT_DIR}
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -I${MY_JNI_HEADERS} ${CPP_FOLDER}/org_neuropod_NeuropodValue.cc -o ${OUT_DIR}/org_neuropod_NeuropodValue.o -std=c++14

org_neuropod_NeuropodValueMap.o: javaclass
	mkdir -p ${OUT_DIR}
	g++ -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin -I${MY_JNI_HEADERS} ${CPP_FOLDER}/org_neuropod_NeuropodValueMap.cc -o ${OUT_DIR}/org_neuropod_NeuropodValueMap.o -std=c++14

libneuropod_jni.dylib: org_neuropod_Neuropod.o org_neuropod_NeuropodValue.o org_neuropod_NeuropodValueMap.o org_neuropod_LibraryLoader.o utils.o jclass_register.o
	g++ -dynamiclib -o ${OUT_DIR}/libneuropod_jni.dylib ${OUT_DIR}/org_neuropod_LibraryLoader.o ${OUT_DIR}/org_neuropod_Neuropod.o ${OUT_DIR}/org_neuropod_NeuropodValue.o ${OUT_DIR}/org_neuropod_NeuropodValueMap.o ${OUT_DIR}/utils.o ${OUT_DIR}/jclass_register.o -lneuropod -framework JavaVM
	install_name_tool -change bazel-out/darwin-fastbuild/bin/neuropod/libneuropod.so /usr/local/lib/libneuropod.so ${OUT_DIR}/libneuropod_jni.dylib


run: libneuropod_jni.dylib
	java -cp ${OUT_DIR} -Djava.library.path=${OUT_DIR} org.neuropod.TestMain

jar: libneuropod_jni.dylib
	jar cvfm test.jar ${PWD}/src/main/resources/META-INF/MANIFEST.MF -C ./out/ .
	java -jar test.jar

clean:
	rm -rf ${OUT_DIR}

