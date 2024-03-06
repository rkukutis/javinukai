import { useState } from "react";
import { Photo } from "../photo/Photo";
import EnlargedPhotoCarousel from "../photo/EnlargedPhotoCarousel";
import { useTranslation } from "react-i18next";
import LikeButton from "../LikeButton";

export default function ContestEntryJury({ entry, index, categoryType }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [fullScreenPhoto, setFullScreenPhoto] = useState(null);
  const { t } = useTranslation();
  const [isLiked, setIsLiked] = useState(false);

  return (
    <div
      className={`py-2 px-6 rounded-md bg-white shadow hover:cursor-pointer border-2 border-white hover:border-teal-500 ${
        isExpanded ? "border-teal-500 border-2" : ""
      }`}
    >
      <div
        className="flex justify-between items-center"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <div className="flex space-x-3 items-center">
          <span className="font-bold text-md md:text-md xl:text-xl text-teal-500">
            {index + 1}
          </span>
          <span className="wrap text-md md:text-lg xl:text-1xl text-slate-600">
            {entry.name}
          </span>
          {categoryType !== "SINGLE" && (
            <span className="hidden md:inline">
              ({entry.images.length} {t("ContestEntry.photos")})
            </span>
          )}
        </div>
      </div>
      {isExpanded && (
        <div>
          <p className="text whitespace-pre-wrap mt-4">{entry.description}</p>
          <div className="flex flex-col xl:grid xl:grid-cols-3 w-full gap-6 my-4">
            {entry.images.map((image) => (
              <Photo
                key={image.id}
                photo={image}
                onClick={() => setFullScreenPhoto(image.id)}
              />
            ))}
          </div>
          <LikeButton isLiked={isLiked} setIsLiked={setIsLiked} />
          {fullScreenPhoto && (
            <EnlargedPhotoCarousel
              description={entry.description}
              photos={entry.images}
              startingPhotoId={fullScreenPhoto}
              onClose={setFullScreenPhoto}
            />
          )}
        </div>
      )}
    </div>
  );
}
