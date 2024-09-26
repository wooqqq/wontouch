import FriendInfo from "../common/FriendInfo";
import CancelIcon from "../../assets/Icon/cancel.png";

function FriendInvite() {
  return (
    <div className="yellow-box p-[37px] h-[400px]">
      <div className="mint-title">친구 초대</div>
      <button>
        <img src={CancelIcon} alt="닫기 버튼" />
      </button>
      <section className="bg-[#E6E2C2] h-2/3 pl-[14px] pr-[30px] py-5 rounded-[10px] overflow-x-hidden overflow-y-scroll">
        <div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
          <div className="flex mb-4">
            <FriendInfo />
            <button>초대</button>
          </div>
        </div>
      </section>
    </div>
  );
}

export default FriendInvite;
