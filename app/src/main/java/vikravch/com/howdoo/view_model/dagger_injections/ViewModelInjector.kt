package vikravch.com.howdoo.view_model.dagger_injections

import dagger.Component
import vikravch.com.howdoo.view_model.core.ItemViewModel
import vikravch.com.howdoo.view_model.core.MainActivityViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(itemViewModel: ItemViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
    }
}