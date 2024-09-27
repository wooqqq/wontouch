import { createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import { RootState } from '../store';

interface FetchUserByIdParams {
  userId: number;
  accessToken: string;
}

export const fetchUserById = createAsyncThunk(
  'users/fetchById',
  async ({ userId, accessToken }: FetchUserByIdParams) => {
    const response = await axios.get(
      `http://localhost:8081/api/user/${userId}`,
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      },
    );

    return response.data; // 사용자 정보 반환
  },
);
