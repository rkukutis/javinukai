import { useState } from "react";
import { Photo } from "../photo/Photo";
import LikeButton from "../LikeButton";

export default function ContestEntryAnon({ entry }) {
  const [isLiked, setIsLiked] = useState(false);

  return (
    <div className="border-2 p-4 rounded-md">
      <div className="flex flex-col xl:grid xl:grid-cols-3">
        <div>
          {entry.images.map((image) => (
            <Photo key={image.id} photo={image} />
          ))}
          <LikeButton isLiked={isLiked} setIsLiked={setIsLiked} />
        </div>
      </div>
    </div>
  );
}
