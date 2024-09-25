import { useState } from "react";
import axios from "axios";
import basicCharacter from "../../../assets/Login/basicCharacter.png";
import "./SignupWithKakao.css";

function SignupWithKakao() {
  const [nickname, setNickname] = useState("");
  const [isNicknameAvailable, setIsNicknameAvailable] = useState<
    boolean | null
  >(null);

  const handleSubmit = (event: React.FocusEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!nickname) {
      alert("닉네임을 입력해주세요.");
      return;
    }
    if (isNicknameAvailable === false) {
      alert("중복된 닉네임입니다. 다른 닉네임을 입력해주세요.");
      return;
    }

    console.log("여기까지 왔다면 회원가입 진행. nickname:", nickname);
  };

  const handleCheckNickname = async () => {
    if (!nickname) {
      alert("닉네임을 입력해주세요.");
      return;
    }

    // 닉네임 중복 체크
    // 임의로 넣은 true, false가 아닌 중복확인 요청으로 받은 값을 넣어야 함.
    try {
      // const response = await axios.get
      if (true) {
        setIsNicknameAvailable(true);
      } else {
        setIsNicknameAvailable(false);
      }
    } catch {}
  };

  return (
    <div>
      <div className="profile-create">캐릭터생성</div>
      <div>
        <div className="profile-box">
          <img className="profile-img" src={basicCharacter} alt="기본 캐릭터" />
        </div>
        <div>
          <form onSubmit={handleSubmit}>
            <label htmlFor="nickname" style={{ marginRight: "30px" }}>
              닉네임
            </label>
            <input
              className="font-['Galmuri11']"
              style={{ width: "400px" }}
              id="nickname"
              placeholder="한글, 숫자, 영문 입력 가능 (한글 기준 최대 6자)"
              value={nickname}
              onChange={(event) => setNickname(event.target.value)}
            />
            <button type="button" onClick={handleCheckNickname}>
              중복확인
            </button>
            <div className="font-['Galmuri11']">
              {isNicknameAvailable === true && <p>사용 가능한 닉네임입니다.</p>}
              {isNicknameAvailable === false && (
                <p>이미 사용 중인 닉네임입니다.</p>
              )}
            </div>
            <button type="submit" style={{ marginTop: "30px" }}>
              생성
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignupWithKakao;
