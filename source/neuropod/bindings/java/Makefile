export MY_JNI_HEADERS=${PWD}/src/main/native
export PROJECT_PACKAGE=${PWD}/src/main/java/com/uber/neuropod

export OUT_DIR=${PWD}/out

genHeaders:
	mkdir -p ${OUT_DIR}
	javac -source 1.8 -h ${MY_JNI_HEADERS} -d ${OUT_DIR} ${PROJECT_PACKAGE}/*.java
	rm -rf ${OUT_DIR}
