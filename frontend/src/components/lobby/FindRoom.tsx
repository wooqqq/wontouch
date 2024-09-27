import React from 'react';

const API_LINK = import.meta.env.VITE_API_URL;

export default function FindRoom({
  closeFindRoom,
}: {
  closeFindRoom: () => void;
}) {
  return (
    <div>
      <div>
        <button>방 찾기</button>
      </div>
    </div>
  );
}
