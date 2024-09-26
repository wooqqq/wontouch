function RoomChat() {
  return (
    <div className="waitingroom-brown-box py-0 pl-3">
      <section>
        <div>채팅 내용</div>
        <div>채팅 내용</div>
        <div>채팅 내용</div>
        <div>채팅 내용</div>
      </section>

      <form action="" method="POST">
        <input
          type="text"
          placeholder="메시지를 입력하세요"
          className="bg-[#FFF2D1] rounded-[10px] p-2 w-4/5 focus:outline-none"
        />
        <button>전송</button>
      </form>
    </div>
  );
}

export default RoomChat;
