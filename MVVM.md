# MVVM

## Lifecycles
生命周期感知组件
LifecycleRegistry
LifecycleOwner
LifecycleObserver

## ViewModel
存储和管理与UI相关的数据
他自身会被存放在 ViewModelStore，本质是 HashMap
没什么特别的，就一个界面关闭时清楚数据

## LiveData
可带有生命周期感知的观察者，可被 Observer 观察
内部看到一个类 ArchTaskExecutor，在什么线程执行Runnable

-----
activity 存放 ViewModel,
ViewModel 存放 LiveData

## databinding
默认是改变数据然后可以改变界面
调用 notifyChange 即可改变界面，
不然的话调用 databinding.set
如果想改变界面后改变数据，也就是双向绑定，用 @={}

binding.setLifecycleOwner(this),有空看看这个的作用