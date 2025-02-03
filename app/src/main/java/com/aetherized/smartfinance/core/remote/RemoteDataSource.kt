package com.aetherized.smartfinance.core.remote

interface RemoteDataSource {
    suspend fun syncData()
}

class RemoteDataSourceImpl : RemoteDataSource{
    override suspend fun syncData() {
        println("Remote sync is not implemented yet.")
        TODO("Not yet implemented")
    }
}
