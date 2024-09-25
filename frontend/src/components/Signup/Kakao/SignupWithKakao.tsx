import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useSelector } from "react-redux";
import { RootState } from "../../../redux/store";

import basicCharacter from "../../../assets/Login/basicCharacter.png";
import "./SignupWithKakao.css";

const API_LINK = import.meta.env.VITE_API_URL;

function SignupWithKakao() {
  const navigate = useNavigate();

  const [nickname, setNickname] = useState("");
  const [isNicknameAvailable, setIsNicknameAvailable] = useState<
    boolean | null
  >(null);

  const handleCheckNickname = async () => {
    if (!nickname) {
      alert("닉네임을 입력해주세요.");
      return;
    }

    // 닉네임 중복 체크
    // 임의로 넣은 true, false가 아닌 중복확인 요청으로 받은 값을 넣어야 함.
    try {
      const response = await axios.post(
        `${API_LINK}/user-profile/nickname/duplicate-check`,
        {
          nickname: nickname,
        },
      );

      console.log(response);

      if (true) {
        setIsNicknameAvailable(true);
      } else {
        setIsNicknameAvailable(false);
      }
    } catch {}
  };

  // store에 저장된 userId와 입력받은 nickname을 이용해서 회원가입 진행
  const userId = useSelector((state: RootState) => state.user.id);
  const signupWithKakao = async () => {
    if (!nickname) {
      alert("닉네임을 입력해주세요.");
      return;
    }
    if (isNicknameAvailable === false) {
      alert("중복된 닉네임입니다. 다른 닉네임을 입력해주세요.");
      return;
    }

    console.log(nickname);
    console.log(userId);

    const res = await axios.post(`${API_LINK}/user-profile/join`, {
      userId: userId,
      nickname: nickname,
    });
  };

  return (
    <div>
      <div className="profile-create">캐릭터생성</div>
      <div>
        <div className="profile-box">
          <img className="profile-img" src={basicCharacter} alt="기본 캐릭터" />
        </div>
        <div>
          <form onSubmit={signupWithKakao}>
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
            <button type="submit">생성</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignupWithKakao;
