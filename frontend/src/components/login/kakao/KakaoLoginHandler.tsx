import { useEffect } from 'react';
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

import flowerGirl from '../../../assets/background/characters/stand/flower_girl.png';

interface DecodedToken {
  userId: number;
}

function KakaoLoginHandler() {
  const AUTH_LINK = import.meta.env.VITE_AUTH_URL;
  const API_LINK = import.meta.env.VITE_API_URL;

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const getToken = async () => {
    const code = new URL(window.location.href).searchParams.get('code'); // 인가코드 추출

    try {
      // 백엔드로부터 access token을 받아옴
      const userRes = await axios.post(`${AUTH_LINK}/oauth/kakao`, {
        code: code,
      });

      // access token, kakao access token을 local storage에 저장
      const accessToken = userRes.data.data.accessToken;
      const kakaoAccessToken = userRes.data.data.kakaoAccessToken;
      localStorage.setItem('access_token', accessToken);
      localStorage.setItem('kakao_access_token', kakaoAccessToken);

      // access token을 디코딩해 userId 추출, store에 저장
      const decodedToken = jwtDecode<DecodedToken>(accessToken);
      const userId = decodedToken.userId;
      dispatch(setUserId(userId));

      // 회원가입이 필요한 경우
      if (userRes.data.data.firstLogin === true) {
        setTimeout(() => {
          navigate('/signup');
        }, 1500);
      } else {
        setTimeout(() => {
          // 이미 가입된 유저인 경우 사용자 정보 store에 저장 후, sse 연결하고 로비로 이동
          getUserData(userId, accessToken);
        }, 1500);
      }
    } catch (error) {
      console.log(error);
      alert('로그인 중 오류가 발생했습니다. 다시 시도해주세요.');
    }
  };

  // 사용자 정보 가져오기
  const getUserData = async (userId: number, accessToken: string) => {
    try {
      // 사용자 정보 요청
      const response = await axios.get(`${API_LINK}/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      dispatch(setUserNickname(response.data.data.nickname));
      dispatch(setUserDescription(response.data.data.description));
      dispatch(setUserCharacterName(response.data.data.characterName));

      // 로비로 이동
      navigate('/lobby');
    } catch (error) {
      console.log('Error fetching user data:', error);
    }
  };

  // useEffect를 사용하여 getToken 함수 호출
  useEffect(() => {
    getToken();
  }, []);

  return (
    <div className="flex items-center justify-between">
      <div className="mr-14 w-44">
        <img src={flowerGirl} alt="" className="w-full" />
      </div>
      <div className="text-5xl">유저 정보 확인 중 . . .</div>
    </div>
  );
}

export default KakaoLoginHandler;
