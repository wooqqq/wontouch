import bronze1 from '../../assets/level/01_level_bronze.png';
import bronze2 from '../../assets/level/02_level_bronze.png';
import bronze3 from '../../assets/level/03_level_bronze.png';
import silver1 from '../../assets/level/04_level_silver.png';
import silver2 from '../../assets/level/05_level_silver.png';
import silver3 from '../../assets/level/06_level_silver.png';
import gold1 from '../../assets/level/07_level_gold.png';
import gold2 from '../../assets/level/08_level_gold.png';
import gold3 from '../../assets/level/09_level_gold.png';
import platinum1 from '../../assets/level/10_level_platinum.png';
import platinum2 from '../../assets/level/11_level_platinum.png';
import platinum3 from '../../assets/level/12_level_platinum.png';
import dia1 from '../../assets/level/13_level_dia.png';
import dia2 from '../../assets/level/14_level_dia.png';
import dia3 from '../../assets/level/15_level_dia.png';
import ruby1 from '../../assets/level/16_level_ruby.png';
import ruby2 from '../../assets/level/17_level_ruby.png';
import ruby3 from '../../assets/level/18_level_ruby.png';
import master1 from '../../assets/level/19_level_master.png';
import master2 from '../../assets/level/20_level_master.png';
import master3 from '../../assets/level/21_level_master.png';
import grand from '../../assets/level/22_level_grand.png';

interface FriendInfoProps {
  key: number;
  nickname: string;
  description: string;
  characterName: string;
  tierPoint: number;
}

export default function FriendInfo({
  key,
  nickname,
  description,
  characterName,
  tierPoint,
}: FriendInfoProps) {
  return (
    <div className="relative">
      <div className="ranking-box white-text text-2xl h-[40px] w-full mb-1">
        {nickname}
        <div className="absolute right-4">
          {/* <img src={character} alt="" className="w-6 h-7" /> */}
        </div>
      </div>
    </div>
  );
}
