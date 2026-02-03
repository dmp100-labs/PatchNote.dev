package org.patchnote.patchnote.core.di

class SharedModule {
    /**
     * TODO: [DI] Koin 도입 시 아래 Factory들을 이 모듈로 통합
     * - DataSourceFactory → single<UserRemoteDataSource> { ... }
     * - RepositoryFactory → single<UserRepository> { ... }
     * - UseCaseFactory → single { GetUsersUseCase(...) }
     * isServerReady 플래그로 Fake/Real 스위칭
     */
}