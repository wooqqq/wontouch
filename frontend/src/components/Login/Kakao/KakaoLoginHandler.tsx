import React, { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function KakaoLoginHandler() {
  const navigate = useNavigate();
  const API_LINK = import.meta.env.VITE_API_URL;

  const getToken = async () => {
    const code = new URL(window.location.href).searchParams.get("code"); // 인가코드 추출

    // 인가코드가 없다면 오류 처리
    if (!code) {
      console.error("인가코드 확인 불가");
      return;
    }

    try {
      // 카카오 OAuth 서버에서 Access Token 발급 요청
      const tokenRes = await axios.post(
        "https://kauth.kakao.com/oauth/token",
        new URLSearchParams({
          grant_type: "authorization_code",
          client_id: import.meta.env.VITE_API_KEY,
          client_secret: import.meta.env.VITE_SECRET_KEY,
          redirect_uri: import.meta.env.VITE_REDIRECT_URI,
          code: code,
        }),
        {
          headers: {
            "Content-type": "application/x-www-form-urlencoded;charset=utf-8",
          },
        },
      );

      // Access Token을 로컬스토리지에 저장
      localStorage.setItem("token", tokenRes.data.access_token);

      // 백엔드로 토큰을 보내어 회원가입 여부 확인
      // const userRes = await axios.post(
      //   `${API_LINK}/auth/oauth/kakao?token=${tokenRes.data.access_token}`,
      // );

      const userRes = await axios.post(
        `${API_LINK}/auth/oauth/kakao`,
        {
          token: localStorage.getItem("token"),
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        },
      );

      // 회원가입이 필요한 경우
      if (userRes.data.data.firstLogin === true) {
        // 회원가입 페이지로 이동
        navigate("/signupWithKakao");
        //유저 정보 넘어오는것들 state로 한번에 보내기
      } else {
        navigate("/lobby"); // 이미 가입된 유저인 경우 로비로 이동
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getToken(); // 컴포넌트가 렌더링될 때 getToken 함수 호출
  }, []);

  return <div></div>;
}

export default KakaoLoginHandler;
