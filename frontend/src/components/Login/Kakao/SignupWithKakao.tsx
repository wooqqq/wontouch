function SignupWithKakao() {
  const handleSubmit = () => {
    console.log("ㅎㅇㅎㅇ");
  };

  return (
    <div>
      <div>캐릭터생성</div>
      <div>
        <div>
          <img src="src/assets/Login/basicCharacter.png" alt="" />
        </div>
        <div>
          <form onSubmit={handleSubmit}>
            <label>닉네임</label>
            <input id="nickname" />
          </form>
        </div>
        <div>
          <button>생성</button>
        </div>
      </div>
    </div>
  );
}

export default SignupWithKakao;
