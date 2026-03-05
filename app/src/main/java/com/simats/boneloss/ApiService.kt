package com.simats.boneloss

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.GET

interface ApiService {

    @Multipart
    @POST("upload-xray")
    suspend fun uploadXray(
        @Part("patient_id") patientId: okhttp3.RequestBody,
        @Part("user_id") userId: okhttp3.RequestBody,
        @Part file: okhttp3.MultipartBody.Part
    ): Response<AnalysisResponse>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("profile/{user_id}")
    suspend fun getProfile(@Path("user_id") userId: Int): Response<User>

    @GET("dashboard/{user_id}")
    suspend fun getDashboard(@Path("user_id") userId: Int): Response<DashboardResponse>

    @GET("admin/dashboard")
    suspend fun getAdminDashboard(): Response<AdminDashboardResponse>

    @GET("analysis/{analysis_id}")
    suspend fun getAnalysis(@Path("analysis_id") analysisId: Int): Response<AnalysisResponse>

    @GET("clinical-interpretation/{analysis_id}")
    suspend fun getClinicalInterpretation(@Path("analysis_id") analysisId: Int): Response<ClinicalInterpretationResponse>

    @GET("full-report/{analysis_id}")
    suspend fun getFullReport(@Path("analysis_id") analysisId: Int): Response<PatientReport>

    @retrofit2.http.Streaming
    @GET("export-pdf/{analysis_id}")
    suspend fun exportPdf(@Path("analysis_id") analysisId: Int): Response<okhttp3.ResponseBody>

    @GET("history/{user_id}")
    suspend fun getHistory(@Path("user_id") userId: Int): Response<List<HistoryResponse>>
}
