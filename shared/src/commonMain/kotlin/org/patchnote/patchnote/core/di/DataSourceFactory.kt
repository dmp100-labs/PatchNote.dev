package org.patchnote.patchnote.core.di
import org.patchnote.patchnote.data.datasource.remote.FakeUserRemoteDataSource
import org.patchnote.patchnote.data.datasource.remote.UserRemoteDataSource

object DataSourceFactory {

    fun createUserRemoteDataSource(): UserRemoteDataSource {
        // TODO: 추후 Real API 구현 시 여기서 교체
        // return RealUserRemoteDataSource(httpClient)
        return FakeUserRemoteDataSource()
    }
}