import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import {
  setUserId,
  setUserNickname,
  setUserDescription,
  setUserCharacterName,
} from '../../../redux/slices/userSlice';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  userId: number;
}

function KakaoLoginHandler() {
  const AUTH_LINK = import.meta.env.VITE_AUTH_URL;
  const API_LINK = import.meta.env.VITE_API_URL;

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [userData, setUserData] = useState<any>(null);

  const getToken = async () => {
    const code = new URL(window.location.href).searchParams.get('code'); // 인가코드 추출

    try {
      // 백엔드로부터 access token을 받아옴
      const userRes = await axios.post(`${AUTH_LINK}/oauth/kakao`, {
        code: code,
      });

      // access token을 local storage에 저장
      const accessToken = userRes.data.data.accessToken;
      localStorage.setItem('access_token', accessToken);

      // 회원가입이 필요한 경우
      if (userRes.data.data.firstLogin === true) {
        setTimeout(() => {
          // 회원가입 페이지로 이동
          navigate('/signup');
        }, 1500);
      } else {
        setTimeout(() => {
          navigate('/lobby'); // 이미 가입된 유저인 경우 로비로 이동
        }, 1500);
      }
    } catch (error) {
      console.log(error);
      alert('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  useEffect(() => {
    // 컴포넌트가 렌더링될 때 getToken 함수 호출
    getToken();

    const userId = localStorage.getItem('user_id');
    const accessToken = localStorage.getItem('access_token');

    // 만약 userId, access token이 모두 존재한다면 사용자의 정보를 store에 저장하자
    if (userId && accessToken) {
      const getUserInfo = async () => {
        try {
          const response = await axios.get(`${API_LINK}/user/${userId}`, {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          });

          dispatch(setUserId(response.data.data.userId));
          dispatch(setUserNickname(response.data.data.nickname));
          dispatch(setUserDescription(response.data.data.description));
          dispatch(setUserCharacterName(response.data.data.characterName));
        } catch (error) {
          console.log('유저 정보 불러오기 실패', error);
        }
      };

      getUserInfo();
    }
  }, [dispatch, API_LINK]);

  return <div>유저 정보 확인 중 . . .</div>;
}

export default KakaoLoginHandler;
