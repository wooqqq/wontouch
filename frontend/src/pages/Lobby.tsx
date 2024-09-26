import { RootState } from "../redux/store";
import { useSelector } from "react-redux";
import FindRoom from "../components/Lobby/FindRoom";
import MakeRoom from "../components/Lobby/MakeRoom";

function Lobby() {
  const nickname = useSelector((state: RootState) => state.user.nickname);

  return (
    <div>
      <div>로비입니당</div>
      <div>{nickname}님 반갑습니다!</div>
      <div>
        <FindRoom />
      </div>
      <div>
        <MakeRoom />
      </div>
    </div>
  );
}

export default Lobby;
