import { useNavigate } from "react-router-dom";

function KakaoToSignup() {
  const navigate = useNavigate();

  const goToSignup = async () => {
    navigate("/signup/kakao");
  };

  return (
    <div>
      <div>캐릭터 생성하러 가볼까요?</div>
      <div>
        <button onClick={goToSignup}>캐릭터 생성</button>
      </div>
    </div>
  );
}

export default KakaoToSignup;
