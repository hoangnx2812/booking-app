import apiClient from './axios';
import { sha256 } from '../utils/hash';
import type { BaseRequest, BaseResponse, LoginUserRequest, LoginUserResponse } from '../types/api';

export const loginApi = async (username: string, password: string): Promise<BaseResponse<LoginUserResponse>> => {
  const hashedPassword = await sha256(password);
  const request: BaseRequest<LoginUserRequest> = {
    data: { username, password: hashedPassword },
  };
  const response = await apiClient.post<BaseResponse<LoginUserResponse>>(
    '/authentication/auth/login',
    request
  );
  return response.data;
};
