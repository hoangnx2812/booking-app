package com.example.commericalcommon.utils;

public class AuthorityConstant {

    public static class DefaultRole {
        public static final String ADMIN = "hasAuthority('" + Constant.DefaultRole.ADMIN + "')";
        public static final String USER = "hasAuthority('" + Constant.DefaultRole.USER + "')";
        public static final String ARTIST = "hasAuthority('" + Constant.DefaultRole.ARTIST + "')";
    }

    // ===============================
    // AUTH
    // ===============================
    public static class Auth {
        public static final String AUTH_DELETE_ACCOUNT_INIT = "hasAuthority('" + PermissionConstant.Auth.DELETE_ACCOUNT_INIT + "')";
        public static final String AUTH_DELETE_ACCOUNT_CONFIRM = "hasAuthority('" + PermissionConstant.Auth.DELETE_ACCOUNT_CONFIRM + "')";
        public static final String AUTH_REGISTER_ARTIST = "hasAuthority('" + PermissionConstant.Auth.REGISTER_ARTIST + "')";
        public static final String AUTH_REGISTER_DEVICE = "hasAuthority('" + PermissionConstant.Auth.REGISTER_DEVICE + "')";
        public static final String AUTH_REGISTER_BIOMETRIC = "hasAuthority('" + PermissionConstant.Auth.REGISTER_BIOMETRIC + "')";
    }

    // ===============================
    // USER PROFILE
    // ===============================
    public static class UserProfile {
        public static final String USER_PROFILE_VIEW = "hasAuthority('" + PermissionConstant.UserProfile.VIEW + "')";
        public static final String USER_PROFILE_UPDATE = "hasAuthority('" + PermissionConstant.UserProfile.UPDATE + "')";
    }

    // ===============================
    // POST
    // ===============================
    public static class Post {
        public static final String POST_LIST_VIEW = "hasAuthority('" + PermissionConstant.Post.LIST_VIEW + "')";
        public static final String POST_DETAIL_VIEW = "hasAuthority('" + PermissionConstant.Post.DETAIL_VIEW + "')";
        public static final String POST_RATE_VIEW = "hasAuthority('" + PermissionConstant.Post.RATE_VIEW + "')";
        public static final String POST_RATE_CREATE = "hasAuthority('" + PermissionConstant.Post.RATE_CREATE + "')";
        public static final String POST_FAVORITE_TOGGLE = "hasAuthority('" + PermissionConstant.Post.FAVORITE_TOGGLE + "')";
        public static final String POST_BLOCK = "hasAuthority('" + PermissionConstant.Post.BLOCK + "')";
        public static final String POST_REPORT = "hasAuthority('" + PermissionConstant.Post.REPORT + "')";
        public static final String POST_SHARE = "hasAuthority('" + PermissionConstant.Post.SHARE + "')";
        public static final String POST_FAVORITE_LIST_VIEW = "hasAuthority('" + PermissionConstant.Post.FAVORITE_LIST_VIEW + "')";
    }

    // ===============================
    // MESSAGE
    // ===============================
    public static class Message {
        public static final String MESSAGE_HISTORY_VIEW = "hasAuthority('" + PermissionConstant.Message.HISTORY_VIEW + "')";
        public static final String MESSAGE_MARK_ALL_READ = "hasAuthority('" + PermissionConstant.Message.MARK_ALL_READ + "')";
        public static final String MESSAGE_SEND = "hasAuthority('" + PermissionConstant.Message.SEND + "')";
    }

    // ===============================
    // ARTIST
    // ===============================
    public static class Artist {
        public static final String ARTIST_LIST_VIEW = "hasAuthority('" + PermissionConstant.Artist.LIST_VIEW + "')";
        public static final String ARTIST_PROFILE_VIEW = "hasAuthority('" + PermissionConstant.Artist.PROFILE_VIEW + "')";
        public static final String ARTIST_BLOCK = "hasAuthority('" + PermissionConstant.Artist.BLOCK + "')";
        public static final String ARTIST_REPORT = "hasAuthority('" + PermissionConstant.Artist.REPORT + "')";
        public static final String ARTIST_SHARE = "hasAuthority('" + PermissionConstant.Artist.SHARE + "')";
        public static final String ARTIST_POST_LIST_VIEW = "hasAuthority('" + PermissionConstant.Artist.POST_LIST_VIEW + "')";
        public static final String ARTIST_SERVICE_LIST_VIEW = "hasAuthority('" + PermissionConstant.Artist.SERVICE_LIST_VIEW + "')";
        public static final String ARTIST_SERVICE_DETAIL_VIEW = "hasAuthority('" + PermissionConstant.Artist.SERVICE_DETAIL_VIEW + "')";
        public static final String ARTIST_REVIEW_LIST_VIEW = "hasAuthority('" + PermissionConstant.Artist.REVIEW_LIST_VIEW + "')";
        public static final String ARTIST_REVIEW_CREATE = "hasAuthority('" + PermissionConstant.Artist.REVIEW_CREATE + "')";

        public static final String ARTIST_POST_CREATE = "hasAuthority('" + PermissionConstant.Artist.POST_CREATE + "')";
        public static final String ARTIST_POST_UPDATE = "hasAuthority('" + PermissionConstant.Artist.POST_UPDATE + "')";
        public static final String ARTIST_POST_DELETE = "hasAuthority('" + PermissionConstant.Artist.POST_DELETE + "')";

        public static final String ARTIST_SERVICE_CREATE = "hasAuthority('" + PermissionConstant.Artist.SERVICE_CREATE + "')";
        public static final String ARTIST_SERVICE_UPDATE = "hasAuthority('" + PermissionConstant.Artist.SERVICE_UPDATE + "')";
        public static final String ARTIST_SERVICE_DELETE = "hasAuthority('" + PermissionConstant.Artist.SERVICE_DELETE + "')";

        public static final String ARTIST_PROMOTION_CREATE = "hasAuthority('" + PermissionConstant.Artist.PROMOTION_CREATE + "')";
        public static final String ARTIST_PROMOTION_UPDATE = "hasAuthority('" + PermissionConstant.Artist.PROMOTION_UPDATE + "')";
        public static final String ARTIST_PROMOTION_DELETE = "hasAuthority('" + PermissionConstant.Artist.PROMOTION_DELETE + "')";

        public static final String ARTIST_PROFILE_UPDATE = "hasAuthority('" + PermissionConstant.Artist.PROFILE_UPDATE + "')";
    }

    // ===============================
    // BOOKING
    // ===============================
    public static class Booking {
        public static final String BOOKING_USER_HISTORY_VIEW = "hasAuthority('" + PermissionConstant.Booking.USER_HISTORY_VIEW + "')";
        public static final String BOOKING_USER_CREATE = "hasAuthority('" + PermissionConstant.Booking.USER_CREATE + "')";
        public static final String BOOKING_ARTIST_LIST_VIEW = "hasAuthority('" + PermissionConstant.Booking.ARTIST_LIST_VIEW + "')";
    }

    // ===============================
    // NOTIFICATION
    // ===============================
    public static class Notification {
        public static final String NOTIFICATION_USER_HISTORY_VIEW = "hasAuthority('" + PermissionConstant.Notification.USER_HISTORY_VIEW + "')";
        public static final String NOTIFICATION_BOOKING_ARTIST_VIEW = "hasAuthority('" + PermissionConstant.Notification.BOOKING_ARTIST_VIEW + "')";
    }

    // ===============================
    // ADMIN
    // ===============================
    public static class Admin {
        public static final String ADMIN_REPORTED_POST_LIST_VIEW = "hasAuthority('" + PermissionConstant.Admin.REPORTED_POST_LIST_VIEW + "')";
        public static final String ADMIN_REPORTED_ARTIST_LIST_VIEW = "hasAuthority('" + PermissionConstant.Admin.REPORTED_ARTIST_LIST_VIEW + "')";
        public static final String ADMIN_REPORTED_POST_DELETE = "hasAuthority('" + PermissionConstant.Admin.REPORTED_POST_DELETE + "')";
        public static final String ADMIN_REPORTED_ARTIST_LOCK = "hasAuthority('" + PermissionConstant.Admin.REPORTED_ARTIST_LOCK + "')";
        public static final String ADMIN_ARTIST_REGISTRATION_LIST_VIEW = "hasAuthority('" + PermissionConstant.Admin.ARTIST_REGISTRATION_LIST_VIEW + "')";
        public static final String ADMIN_ARTIST_REGISTRATION_APPROVE = "hasAuthority('" + PermissionConstant.Admin.ARTIST_REGISTRATION_APPROVE + "')";
        public static final String ADMIN_ARTIST_REGISTRATION_REJECT = "hasAuthority('" + PermissionConstant.Admin.ARTIST_REGISTRATION_REJECT + "')";
    }
}
