import { useState } from "react";
import EnlargedPhotoCarousel from "../photo/EnlargedPhotoCarousel";
import { useTranslation } from "react-i18next";
import LikeButton from "../LikeButton";
import SeriesPhotoCollection from "../photo/SeriesPhotoCollection";

export default function SeriesEntry({ entry }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [fullScreenPhoto, setFullScreenPhoto] = useState(null);
  const { t } = useTranslation();
  const [isLiked, setIsLiked] = useState(entry.isLiked);
  return (
    <div>
      <p className="text whitespace-pre-wrap mt-4">
        {entry.collection.description}
      </p>
      <div className="xl:grid xl:grid-cols-3">
        {entry?.collection.images.map((image) => (
          <img key={image.id} src={image.urlSmall} />
        ))}
      </div>
      <LikeButton isLiked={isLiked} setIsLiked={setIsLiked} />
      {fullScreenPhoto && (
        <EnlargedPhotoCarousel
          description={entry.collection.description}
          photos={entry.collection.images}
          startingPhotoId={fullScreenPhoto}
          onClose={setFullScreenPhoto}
        />
      )}
    </div>
  );
}
