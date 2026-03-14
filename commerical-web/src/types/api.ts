// Mirrors backend BaseRequest/BaseResponse wrappers

export interface BaseRequest<T> {
  requestId?: string;
  sessionId?: string;
  channelCode?: string;
  data: T;
}

export interface BaseResponse<T> {
  resultCode: string;
  resultDesc: string;
  requestId?: string;
  data: T;
}

export interface LoginUserRequest {
  username: string;
  password: string;
}

export interface LoginUserResponse {
  accessToken: string;
  refreshToken: string;
  roles: string[];
  permissions: string[];
}
