import { useNavigate } from "react-router-dom";

function KakaoToSignup() {
  const navigate = useNavigate();

  const handleToSignup = async () => {
    navigate("/signup/kakao");
  };

  return (
    <div>
      <div>아직 회원이 아니시군요?</div>
      <div>
        <button onClick={handleToSignup}>회원가입 하러가기</button>
      </div>
    </div>
  );
}

export default KakaoToSignup;
