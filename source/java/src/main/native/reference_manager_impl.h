namespace neuropod
{
namespace jni
{
template <typename T>
void ReferenceManager<T>::put(std::shared_ptr<T> pointer)
{
    if (!holder_)
    {
        holder_ = std::make_shared<std::unordered_map<int64_t, std::vector<std::shared_ptr<T>>>>();
    }
    (*holder_)[reinterpret_cast<int64_t>(pointer.get())].emplace_back(std::move(pointer));
}

template <typename T>
void ReferenceManager<T>::remove(std::shared_ptr<T> pointer)
{
    remove(reinterpret_cast<int64_t>(pointer.get()));
}

template <typename T>
void ReferenceManager<T>::remove(int64_t handle)
{
    if (!contains(handle)) {
        return;
    }
    (*holder_)[handle].back().reset();
    (*holder_)[handle].pop_back();
    if ((*holder_)[handle].empty()) {
        (*holder_).erase(handle);
    }
}

template <typename T>
void ReferenceManager<T>::check(int64_t handle)
{
    if (!contains(handle))
    {
        throw std::runtime_error("Object is not allocated!");
    }

}

template <typename T>
bool ReferenceManager<T>::contains(int64_t handle)
{
    if (!holder_)
    {
        return false;
    }
    if ((*holder_).count(handle) == 0)
    {
        return false;
    }
    if ((*holder_)[handle].empty())
    {
        return false;
    }
    return true;
}

} // namespace jni
} // namespace neuropod
