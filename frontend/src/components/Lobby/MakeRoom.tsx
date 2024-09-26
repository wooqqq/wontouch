import axios from "axios";
import React, { useState } from "react";

const API_LINK = import.meta.env.VITE_API_URL;

function MakeRoom() {
  // UUID 값을 state로 관리해보자
  const [UUID, setUUID] = useState<string>("");
  const [roomName, setRoomName] = useState<string>("");
  const [isPrivate, setIsPrivate] = useState<boolean>(false);
  const [password, setPassword] = useState<string>("");
  const [showModal, setShowModal] = useState<boolean>(false);

  const getUUID = async () => {
    try {
      const res = await axios.get(`${API_LINK}/room/create/random-uuid`);
      setUUID(res.data.data);
    } catch (error) {
      console.error("UUID 발급 중 에러 발생", error);
    }
  };

  const handleMakeRoom = async () => {
    try {
      const res = await axios.post(`${API_LINK}/room`, {
        roomId: UUID,
      });
    } catch {}
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
