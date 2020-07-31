namespace neuropod
{
namespace jni
{

template <typename T>
jobject createDirectBuffer(JNIEnv *env, NeuropodTensor *tensor)
{
    auto         typedTensor = tensor->as_typed_tensor<T>();
    auto         rawPtr      = typedTensor->get_raw_data_ptr();
    const size_t memSize     = sizeof(T) * tensor->get_num_elements();
    return env->NewDirectByteBuffer(rawPtr, static_cast<jlong>(memSize));
}

template <size_t N>
std::string accessStringTensorAtIndex(TypedNeuropodTensor<std::string> *tensor, int64_t pos) {
    if (pos>=tensor->get_num_elements())  {
        throw std::runtime_error("Index out of boundry");
    }
    auto accessor = tensor->accessor<N>();
    auto iter     = accessor.begin();
    for (int64_t i=0 ; i<pos; iter = iter.operator++(), i++){
        continue;
    }
    std::string res = *iter;
    return res;
}

}
}
