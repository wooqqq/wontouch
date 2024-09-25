import React, { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setUserId } from "../../../redux/slices/userSlice";
import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  userId: number;
}

function KakaoLoginHandler() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const AUTH_LINK = import.meta.env.VITE_AUTH_URL;

  const getToken = async () => {
    const code = new URL(window.location.href).searchParams.get("code"); // 인가코드 추출

    try {
      // 백엔드로부터 access token을 받아옴
      const userRes = await axios.post(`${AUTH_LINK}/auth/oauth/kakao`, {
        code: code,
      });

      // access token을 local storage에 저장
      const accessToken = userRes.data.data.accessToken;
      localStorage.setItem("access_token", accessToken);

      // access token을 디코딩해 userId 추출, store에 저장
      const decodedToken = jwtDecode<DecodedToken>(accessToken);
      dispatch(setUserId(decodedToken.userId));

      // 회원가입이 필요한 경우
      if (userRes.data.data.firstLogin === true) {
        // 회원가입 페이지로 이동
        navigate("/signup");
      } else {
        navigate("/lobby"); // 이미 가입된 유저인 경우 로비로 이동
      }
    } catch (error) {
      console.log(error);
      alert("로그인 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
  };

  useEffect(() => {
    getToken(); // 컴포넌트가 렌더링될 때 getToken 함수 호출
  }, []);

  return <div>로그인 중!</div>;
}

export default KakaoLoginHandler;
