export const CheckSetNickname = (nickname: string) => {
  const isKoreanConsonants = /^[ㄱ-ㅎ]+$/.test(nickname); // 한글 자음만
  const isKoreanVowels = /^[ㅏ-ㅣ]+$/.test(nickname); // 한글 모음만
  const isEnglish = /^[a-zA-Z]+$/.test(nickname); // 영어만
  const isNumeric = /^[0-9]+$/.test(nickname); // 숫자만
  const isKoreanAndNumeric = /^[가-힣0-9]+$/.test(nickname); // 한글 + 숫자
  const isEnglishAndNumeric = /^[a-zA-Z0-9]+$/.test(nickname); // 영어 + 숫자
  const isValidNickname = /^[가-힣a-zA-Z0-9]+$/.test(nickname); // 한글, 영어, 숫자 혼합 가능

  // 한글 + 한글 자음
  const koreanLetters = nickname.match(/[가-힣]/g); // 한글 문자 추출
  const koreanConsonants = nickname.match(/[ㄱ-ㅎ]/g); // 한글 자음 추출
  const koreanVowels = nickname.match(/[ㅏ-ㅣ]/g); // 한글 모음 추출

  const totalLength =
    (koreanLetters ? koreanLetters.length : 0) +
    (koreanConsonants ? koreanConsonants.length : 0) +
    (koreanVowels ? koreanVowels.length : 0);

  // 유효성 검사
  if (isKoreanConsonants) {
    alert('한글 자음만 입력할 수 없습니다.');
    return false;
  } else if (isKoreanVowels) {
    alert('한글 모음만 입력할 수 없습니다.');
    return false;
  } else if (totalLength > 6) {
    alert('한글은 최대 6자까지 입력할 수 있습니다.');
    return false;
  } else if (isEnglish && nickname.length > 10) {
    alert('영어는 최대 10자까지 입력할 수 있습니다.');
    return false;
  } else if (isNumeric && nickname.length > 10) {
    alert('숫자는 최대 10자까지 입력할 수 있습니다.');
    return false;
  } else if (isKoreanAndNumeric && nickname.length > 8) {
    alert('한글과 숫자 조합은 최대 8자까지 입력할 수 있습니다.');
    return false;
  } else if (isEnglishAndNumeric && nickname.length > 10) {
    alert('영어와 숫자 조합은 최대 10자까지 입력할 수 있습니다.');
    return false;
  } else if (isValidNickname && nickname.length > 8) {
    alert('한글, 영어, 숫자 혼합은 최대 8자까지 입력할 수 있습니다.');
    return false;
  } else {
    return true;
  }
};
