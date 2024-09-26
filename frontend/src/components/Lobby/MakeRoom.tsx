import axios from "axios";
import React from "react";

const API_LINK = import.meta.env.VITE_API_URL;

function MakeRoom() {
  const getUUID = async () => {
    try {
      const res = await axios.get(`${API_LINK}/room/create/random-uuid`);
      console.log(res.data.data);
    } catch (error) {
      console.error("UUID 발급 중 에러 발생", error);
    }
  };

  return (
    <div>
      <div>
        <button onClick={getUUID}>방 생성</button>
      </div>
    </div>
  );
}

export default MakeRoom;
