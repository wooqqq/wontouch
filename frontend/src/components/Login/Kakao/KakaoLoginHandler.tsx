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

    const userRes = await axios.post(`${API_LINK}/auth/oauth/kakao`, {
      code: code,
    });

    // 회원가입이 필요한 경우
    if (userRes.data.data.firstLogin === true) {
      // 회원가입 페이지로 이동
      navigate("/signup/kakao");
      //유저 정보 넘어오는것들 state로 한번에 보내기
    } else {
      navigate("/lobby"); // 이미 가입된 유저인 경우 로비로 이동
    }
  };

  useEffect(() => {
    getToken(); // 컴포넌트가 렌더링될 때 getToken 함수 호출
  }, []);

  return <div></div>;
}

export default KakaoLoginHandler;
