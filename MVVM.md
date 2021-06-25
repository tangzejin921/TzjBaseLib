# MVVM

## Lifecycles 观察者模式，对外提供生命周期
生命周期感知组件 Activity里有此集合
LifecycleRegistry::Lifecycle (数据看做：list<LifecycleObserver>,Activity中实例化)
LifecycleObserver ()

## ViewModelStore 跟随生命周期 Activity,Fragment 的数据容器
存储和管理与UI相关的数据
ViewModelStore (数据:Map<String,ViewModel>,Activity,Fragment中实例化)

## LiveData 观察者模式，对外提供数据的变化事件
可带有生命周期感知的被观察者，可被 Observer 观察
LiveData (数据:Map<Observer,ObserverWrapper>)
1.通过 observe ObserverWrapper 包装了 LifecycleObserver，对Lifecycles进行生命周期感知,并自动移除
2.通过 observeForever ObserverWrapper 包装了 AlwaysActiveObserver,需要手动维护监听
注意没有粘性效果?

## ArchTaskExecutor 线程


## databinding
默认是改变数据然后可以改变界面
调用 notifyChange 即可改变界面，
不然的话调用 databinding.set
如果想改变界面后改变数据，也就是双向绑定，用 @={}

binding.setLifecycleOwner(this),有空看看这个的作用