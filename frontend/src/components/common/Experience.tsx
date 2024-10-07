import React from 'react';

function calculateLevel(tierPoint: number): number {
  if (tierPoint >= 100 && tierPoint <= 199) {
    return 200; // 레벨 02
  } else if (tierPoint >= 200 && tierPoint <= 299) {
    return 300; // 레벨 03
  } else if (tierPoint >= 300 && tierPoint <= 399) {
    return 400; // 레벨 04
  } else if (tierPoint >= 400 && tierPoint <= 499) {
    return 500; // 레벨 05
  } else if (tierPoint >= 500 && tierPoint <= 599) {
    return 600; // 레벨 06
  } else if (tierPoint >= 600 && tierPoint <= 699) {
    return 700; // 레벨 07
  } else if (tierPoint >= 700 && tierPoint <= 799) {
    return 800; // 레벨 08
  } else if (tierPoint >= 800 && tierPoint <= 899) {
    return 900; // 레벨 09
  } else if (tierPoint >= 900 && tierPoint <= 999) {
    return 1000; // 레벨 10
  } else if (tierPoint >= 1000 && tierPoint <= 1099) {
    return 1100; // 레벨 11
  } else if (tierPoint >= 1100 && tierPoint <= 1199) {
    return 1200; // 레벨 12
  } else if (tierPoint >= 1200 && tierPoint <= 1299) {
    return 1300; // 레벨 13
  } else if (tierPoint >= 1300 && tierPoint <= 1399) {
    return 1400; // 레벨 14
  } else if (tierPoint >= 1400 && tierPoint <= 1499) {
    return 1500; // 레벨 15
  } else if (tierPoint >= 1500 && tierPoint <= 1599) {
    return 1600; // 레벨 16
  } else if (tierPoint >= 1600 && tierPoint <= 1699) {
    return 1700; // 레벨 17
  } else if (tierPoint >= 1700 && tierPoint <= 1799) {
    return 1800; // 레벨 18
  } else if (tierPoint >= 1800 && tierPoint <= 1899) {
    return 1900; // 레벨 19
  } else if (tierPoint >= 1900 && tierPoint <= 1999) {
    return 2000; // 레벨 20
  } else if (tierPoint >= 2000 && tierPoint <= 2099) {
    return 2100; // 레벨 21
  } else if (tierPoint >= 2100) {
    return 2200; // 레벨 22
  }
  return 100; // 기본 값 (1 레벨)
}

export default function Experience({ tierPoint }: { tierPoint: number }) {
  const level = calculateLevel(tierPoint);
  const percentage = (tierPoint / level) * 100; // 퍼센트 계산

  return (
    <div className="relative mt-6 w-80 h-[35px]">
      <div className="absolute exp-text z-10 w-80 h-[35px]">
        {tierPoint}/{level} ({percentage.toFixed(2)}%)
      </div>
      {/* percentage가 10보다 크거나 같을 때만 exp div 렌더링 */}
      {percentage >= 10 && (
        <div
          className="absolute z-5 h-[35px] exp"
          style={{ width: `${percentage}%` }}
        ></div>
      )}
      <div className="exp-background w-80 h-[35px]"></div>
    </div>
  );
}
