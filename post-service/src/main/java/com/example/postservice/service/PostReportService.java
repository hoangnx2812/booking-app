package com.example.postservice.service;

public interface PostReportService {
    Object addPostReport(Object postReport);

    Object deletePostReport(Object postReport);

    Object getPostReportByConditions(Object postReport);
}
