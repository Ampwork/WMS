package com.ampwork.workdonereportmanagement.network;

import com.ampwork.workdonereportmanagement.model.AddReportModel;
import com.ampwork.workdonereportmanagement.model.ApiResponse;
import com.ampwork.workdonereportmanagement.model.AttendanceReportResponse;
import com.ampwork.workdonereportmanagement.model.ClerkResponse;
import com.ampwork.workdonereportmanagement.model.DailyReportResponse;
import com.ampwork.workdonereportmanagement.model.GenerateReportResponse;
import com.ampwork.workdonereportmanagement.model.ProgramRepose;
import com.ampwork.workdonereportmanagement.model.ReportAttendanceModel;
import com.ampwork.workdonereportmanagement.model.ReportModel;
import com.ampwork.workdonereportmanagement.model.ReportResponse;
import com.ampwork.workdonereportmanagement.model.StudentDetailsModel;
import com.ampwork.workdonereportmanagement.model.StudentResponse;
import com.ampwork.workdonereportmanagement.model.SubjectReportResponse;
import com.ampwork.workdonereportmanagement.model.SubjectResponse;
import com.ampwork.workdonereportmanagement.model.SubmitReportResponse;
import com.ampwork.workdonereportmanagement.model.UserInfo;
import com.ampwork.workdonereportmanagement.model.UsersResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    //public static String BASE_URL = "http://192.168.1.101/wms/";
    public static String BASE_URL = "http://192.168.1.4/wms/";

    @POST("api/login")
    Call<ApiResponse> login(@Body UserInfo userInfo);

    @POST("api/updateProfile")
    Call<ApiResponse> updateProfile(@Body UserInfo userInfo);

    @Multipart
    @POST("api/updateProfilepic")
    Call<ApiResponse> upLoadProfilePic(@Query("id") String id,
                                       @Part MultipartBody.Part file);

    @Multipart
    @POST("api/updateSign")
    Call<ApiResponse> upLoadSign(@Query("id") String id,
                                 @Part MultipartBody.Part file);

    @GET("api/getSubjects")
    Call<SubjectResponse> getSubjects(@Query("pgm_name") String pgm_name);

    @GET("api/getPrograms")
    Call<ProgramRepose> getPrograms();

    @GET("api/getReportListOfUser")
    Call<ReportResponse> getReports(@Query("userid") String userid);

    @POST("api/addReportlist")
    Call<ApiResponse> addReport(@Body AddReportModel addReportModel);

    @POST("api/addReport")
    Call<ReportResponse> createReport(@Body ReportModel addReportModel);

    @GET("api/getDailyReportById")
    Call<DailyReportResponse> getDailyReports(@Query("report_id") String reportId);

    @POST("api/generateFacultyReport")
    Call<GenerateReportResponse> getFacultyReport(@Body GenerateReportResponse generateReportResponse);

    @POST("api/generateMonthlyReport")
    Call<GenerateReportResponse> generateMonthlyReport(@Body GenerateReportResponse generateReportResponse);

    @Multipart
    @POST("api/facultyReportSubmit")
    Call<ApiResponse> facultyReportSubmit(@Body GenerateReportResponse.ReportResponseModel responseModel,
                                          @Part MultipartBody.Part file);

    @POST("api/facultyReportSubmit")
    Call<ApiResponse> facultyReportSubmits(@Body ReportResponse responseModel);

    @GET("api/getCoordinator")
    Call<UsersResponse> getCoordinators();

    @GET("api/getSuperintendent")
    Call<UsersResponse> getSuperintendent();

    @GET("api/getReportByCoordinatorId")
    Call<SubmitReportResponse> getReportByCoordinatorId(@Query("userid") String userid);

    @POST("api/coordinatorSubmit")
    Call<ApiResponse> coordinatorSubmit(@Body ReportResponse responseModel);

    @GET("api/getReportBySupId")
    Call<SubmitReportResponse> getReportBySupId(@Query("userid") String userid);

    @POST("api/superintendentSubmit")
    Call<ApiResponse> superintendentSubmit(@Body ReportResponse responseModel);

    @GET("api/getcount")
    Call<ClerkResponse> getcount();

    @POST("api/addstudent")
    Call<ApiResponse> addstudent(@Body StudentDetailsModel studentDetailsModel);

    @POST("api/editstudent")
    Call<ApiResponse> editstudent(@Body StudentDetailsModel studentDetailsModel);

    @GET("api/getStudentsByProgram")
    Call<StudentResponse> getStudentsByProgram(@Query("program") String program);

    @GET("api/getStudentsBySemester")
    Call<StudentResponse> getStudentsBySemester(@Query("program")String program,
                                              @Query("semester") String semester);

    @POST("api/addReportAttendance")
    Call<ApiResponse> addReportAttendance(@Body List<ReportAttendanceModel> reportAttendanceModels);

    @GET("api/getAttedanceReport")
    Call<AttendanceReportResponse> getAttedanceReport(@Query("report_id") String report_id,
                                                      @Query("userid") String userid);

    @POST("api/addReportSubject")
    Call<ApiResponse> addReportSubject(@Body SubjectReportResponse.SubjectReport subjectReport);
}
