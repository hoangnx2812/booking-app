import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { notifications } from '@mantine/notifications';
import { loginApi } from '../api/auth';

export function useLogin() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: ({ username, password }: { username: string; password: string }) =>
      loginApi(username, password),
    onSuccess: (data) => {
      if (data.resultCode === '00') {
        localStorage.setItem('accessToken', data.data.accessToken);
        localStorage.setItem('refreshToken', data.data.refreshToken);
        navigate('/');
      } else {
        notifications.show({
          title: 'Login Failed',
          message: data.resultDesc || 'Invalid credentials',
          color: 'red',
          autoClose: 4000,
        });
      }
    },
    onError: (error: any) => {
      const message =
        error?.response?.data?.resultDesc || 'Unable to connect to server. Please try again.';
      notifications.show({
        title: 'Error',
        message,
        color: 'red',
        autoClose: 4000,
      });
    },
  });
}

export function useLogout() {
  const navigate = useNavigate();

  return () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    navigate('/login');
  };
}

export function isAuthenticated(): boolean {
  return !!localStorage.getItem('accessToken');
}
