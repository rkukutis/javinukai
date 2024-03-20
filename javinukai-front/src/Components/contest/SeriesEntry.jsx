import { useState } from "react";
import { useTranslation } from "react-i18next";
import LikeButton from "../LikeButton";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import addLike from "../../services/likes/addLike";
import removeLike from "../../services/likes/removeLike";
import Modal from "../Modal";
import backIcon from "../../assets/icons/arrow_back_ios_new_FILL0_wght400_GRAD0_opsz24.svg";
import forwardIcon from "../../assets/icons/arrow_forward_ios_FILL0_wght400_GRAD0_opsz24.svg";
import Button from "../Button";

export default function SeriesEntry({ entry, entries }) {
  const { t } = useTranslation();
  const [isLiked, setIsLiked] = useState(entry.liked);
  const [enlargedPhotoIndex, setEnlargedPhotoIndex] = useState(null);
  const [descriptionVisible, setDescriptionVisible] = useState(false);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const queryClient = useQueryClient();
  const { mutate: addLikeMutation } = useMutation({
    mutationFn: (entryId) => addLike(entryId),
    onSuccess: () => queryClient.invalidateQueries(["contestCategoryEntries"]),
  });
  const { mutate: removeLikeMutation } = useMutation({
    mutationFn: (entryId) => removeLike(entryId),
    onSuccess: () => queryClient.invalidateQueries(["contestCategoryEntries"]),
  });

  function handlePhotoClick(image) {
    const startingIndex = entry.collection.images.indexOf(image);
    setEnlargedPhotoIndex(startingIndex);
    setModalIsOpen(true);
  }

  function handleLikeClick() {
    if (entry.liked) {
      removeLikeMutation(entry.collection.id);
    } else {
      addLikeMutation(entry.collection.id);
    }
  }

  function handleNextEnlargedPhoto() {
    if (enlargedPhotoIndex == entry.collection.images.length - 1) return;
    setEnlargedPhotoIndex(enlargedPhotoIndex + 1);
  }

  function handlePreviousEnlargedPhoto() {
    if (enlargedPhotoIndex == 0) return;
    setEnlargedPhotoIndex(enlargedPhotoIndex - 1);
  }

  return (
    <div className="shadow px-2 py-4 rounded my-2" key={entry.collection.id}>
      <div className="flex xl:flex-row flex-col xl:space-x-3 justify-between">
        <div className="flex xl:flex-row space-y-2 xl:space-y-0 flex-col overflow-auto xl:space-x-2 py-1">
          {entry?.collection.images.map((image) => (
            <img
              onClick={() => handlePhotoClick(image)}
              className="rounded"
              key={image.id}
              src={image.urlThumbnail}
            />
          ))}
        </div>
        <LikeButton
          onClick={handleLikeClick}
          isLiked={isLiked}
          setIsLiked={setIsLiked}
        />
      </div>
      <Modal
        isOpen={modalIsOpen}
        backroundColor="bg-slate-500"
        setIsOpen={setModalIsOpen}
      >
        <div className="relative">
          <img
            className="h-[90vh]"
            src={entry.collection.images[enlargedPhotoIndex || 0].urlFull}
          />
          <Button
            onClick={handlePreviousEnlargedPhoto}
            extraStyle="absolute top-[10%] left-[-1rem] bg-slate-500 hover:bg-slate-400 opacity-75"
          >
            <img src={backIcon} />
          </Button>
          <Button
            onClick={handleNextEnlargedPhoto}
            extraStyle="absolute top-[10%] right-[-1rem] bg-slate-500 hover:bg-slate-400 opacity-75"
          >
            <img src={forwardIcon} />
          </Button>
          <div className="absolute right-2 bottom-2 flex space-x-2">
            <Button onClick={() => setDescriptionVisible(!descriptionVisible)}>
              Description
            </Button>
            <LikeButton isLiked={entry.liked} onClick={handleLikeClick} />
          </div>
          {descriptionVisible && (
            <p className="absolute top-[15%] h-[70%] bg-white w-full p-3 overflow-auto">
              {entry.collection.description}
            </p>
          )}
        </div>
      </Modal>
    </div>
  );
}
