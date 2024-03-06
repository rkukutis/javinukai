import Button from "../Button";
import { useState } from "react";
import backIcon from "../../assets/icons/arrow_back_ios_new_FILL0_wght400_GRAD0_opsz24.svg";
import forwardIcon from "../../assets/icons/arrow_forward_ios_FILL0_wght400_GRAD0_opsz24.svg";
import descriptionIcon from "../../assets/icons/description_FILL0_wght400_GRAD0_opsz24.svg";
import closeIcon from "../../assets/icons/close_FILL0_wght400_GRAD0_opsz24.svg";

export default function EnlargedPhotoCarousel({
  photos,
  description,
  startingPhotoId,
  onClose,
}) {
  const startingPhotoIndex = photos.findIndex(
    (photo) => photo.id == startingPhotoId
  );
  const [displayedPhotoIndex, setDisplayedPhotoIndex] =
    useState(startingPhotoIndex);
  const [descriptionVisible, setDescriptionVisible] = useState(false);

  function handleNextPhoto() {
    if (displayedPhotoIndex === photos.length - 1) return;
    setDisplayedPhotoIndex(displayedPhotoIndex + 1);
  }
  function handlePreviousPhoto() {
    if (displayedPhotoIndex === 0) return;
    setDisplayedPhotoIndex(displayedPhotoIndex - 1);
  }

  return (
    <div className="fixed h-full w-full top-0 left-0 backdrop-blur-xl backdrop-brightness-50 flex justify-center items-center">
      <div className="h-[95vh] relative">
        <img
          className="w-full xl:h-full rounded"
          src={photos.at(displayedPhotoIndex).urlFull}
        />
        <button
          className="bg-slate-900 rounded absolute top-[40%] xl:top-[50%] xl:left-[-5rem] p-3 bg-opacity-50 hover:bg-slate-900"
          onClick={handlePreviousPhoto}
        >
          <img src={backIcon} />
        </button>
        <button
          className="bg-slate-900 p-3 rounded absolute top-[40%] xl:top-[50%] right-0 xl:right-[-5rem] bg-opacity-50 hover:bg-slate-900"
          onClick={handleNextPhoto}
        >
          <img src={forwardIcon} />
        </button>
        <div className="absolute top-5 right-5 flex justify-center space-x-2">
          <Button
            extraStyle="bg-slate-900 p-3 rounded bg-opacity-50 hover:bg-slate-900"
            onClick={() => setDescriptionVisible(!descriptionVisible)}
          >
            <img src={descriptionIcon} />
          </Button>
          <Button
            extraStyle="bg-slate-900 p-3 rounded bg-opacity-50 hover:bg-slate-900"
            onClick={() => onClose(null)}
          >
            <img alt="close photo" src={closeIcon} />
          </Button>
        </div>
        {descriptionVisible && (
          <div className="absolute bottom-0 h-1/2 w-full bg-white bg-opacity-80 p-4 overflow-auto whitespace-pre-wrap leading-relaxed">
            {description}
          </div>
        )}
      </div>
    </div>
  );
}
