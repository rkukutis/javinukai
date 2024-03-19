import whiteHeart from "../assets/icons/favorite_white_FILL0_wght400_GRAD0_opsz24.svg";
import redHeart from "../assets/icons/favorite_red_FILL0_wght400_GRAD0_opsz24.svg";

function LikeButton({ extraStyle, isLiked, onClick }) {
  return (
    <button
      className={`text bg-yellow-200 ${
        isLiked ? "" : ""
      } p-3 rounded transition ${extraStyle}`}
      onClick={onClick}
    >
      <img src={isLiked ? redHeart : whiteHeart} />
    </button>
  );
}

export default LikeButton;
