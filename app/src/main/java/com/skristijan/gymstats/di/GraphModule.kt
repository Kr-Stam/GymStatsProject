package com.skristijan.gymstats.di

import android.content.Context
import com.skristijan.gymstats.R
import com.skristijan.gymstats.util.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GraphModule
{
    @Provides
    @Singleton
    fun provideDateFormatter(): DateValueFormatter{
        return DateValueFormatter()
    }

    @Provides
    @Singleton
    fun provideBlankFormatter(): BlankValueFormatter{
        return BlankValueFormatter()
    }

    @Provides
    @Singleton
    fun provideWeightFormatter(): WeightValueFormatter{
        return WeightValueFormatter()
    }

    @Provides
    @Singleton
    fun provideHighlightMarker(@ApplicationContext context: Context): HighlightMarker{
        return HighlightMarker(context, R.layout.marker_highlight)
    }

    @Provides
    @Singleton
    fun provideSpiderHighlightMarker(@ApplicationContext context: Context): SpiderHighlightMarker{
        return SpiderHighlightMarker(context, R.layout.marker_highlight)
    }
}