import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import Button from "./Button";
import SinglePhotoCollection from "./photo/SinglePhotoCollection";
import Modal from "./Modal";
import backIcon from "../assets/icons/arrow_back_ios_new_FILL0_wght400_GRAD0_opsz24.svg";
import forwardIcon from "../assets/icons/arrow_forward_ios_FILL0_wght400_GRAD0_opsz24.svg";
import LikeButton from "./LikeButton";
import addLike from "../services/likes/addLike";
import removeLike from "../services/likes/removeLike";
import useUserStore from "../stores/userStore";
import { useTranslation } from "react-i18next";

export function SinglesSection({ entries }) {
  const { user } = useUserStore();
  const [enlargedPhotoIndex, setEnlargedPhotoIndex] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const queryClient = useQueryClient();
  const [likedEnlargedEntries, setLikedEnlargedEntries] = useState(
    entries?.content.filter((entry) => entry.liked == true)
  );
  const [descriptionOpen, setDescriptionOpen] = useState(false);
  const { t } = useTranslation();

  const { mutate: addLikeMutation } = useMutation({
    mutationFn: (entryId) => addLike(entryId),
  });
  const { mutate: removeLikeMutation } = useMutation({
    mutationFn: (entryId) => removeLike(entryId),
  });

  function setModalOpenAndRefetch(boolean) {
    setModalOpen(boolean);
    queryClient.invalidateQueries(["contestCategoryEntries"]);
  }

  function handlePhotoClick(entry) {
    const index = entries.content.findIndex(
      (e) => e.collection.id == entry.collection.id
    );
    setEnlargedPhotoIndex(index);
    setModalOpen(true);
  }

  function handleLikeClick() {
    const entry = entries?.content[enlargedPhotoIndex];
    if (likedEnlargedEntries.includes(entry)) {
      removeLikeMutation(entry.collection.id);
      const filtered = likedEnlargedEntries.filter(
        (likedEntry) => likedEntry != entry
      );
      setLikedEnlargedEntries(filtered);
    } else {
      addLikeMutation(entry.collection.id);
      setLikedEnlargedEntries([...likedEnlargedEntries, entry]);
    }
  }

  function handleNextEnlargedPhoto() {
    if (enlargedPhotoIndex == entries.content.length - 1) return;
    setEnlargedPhotoIndex(enlargedPhotoIndex + 1);
  }

  function handlePreviousEnlargedPhoto() {
    if (enlargedPhotoIndex == 0) return;
    setEnlargedPhotoIndex(enlargedPhotoIndex - 1);
  }

  return (
    <>
      <div className="xl:grid-cols-4 xl:grid xl:gap-3 my-3">
        {entries?.content.map((entry) => (
          <SinglePhotoCollection
            key={entry.collection.id}
            entry={entry}
            onClick={() => handlePhotoClick(entry)}
          />
        ))}
      </div>
      <Modal
        backroundColor="bg-slate-500"
        isOpen={modalOpen}
        setIsOpen={setModalOpenAndRefetch}
      >
        <div className="relative">
          <img
            className="h-[90vh]"
            src={
              entries?.content[enlargedPhotoIndex || 0].collection.images[0]
                .urlMiddle
            }
          />
          <Button
            onClick={handlePreviousEnlargedPhoto}
            extraStyle="absolute top-[10%] left-[-1rem] opacity-50 bg-slate-500 hover:bg-slate-400"
          >
            <img src={backIcon} />
          </Button>
          <Button
            onClick={handleNextEnlargedPhoto}
            extraStyle="absolute top-[10%] right-[-1rem] opacity-50 bg-slate-500 hover:bg-slate-400"
          >
            <img src={forwardIcon} />
          </Button>
          <div className="absolute bottom-1 right-1 flex space-x-2">
            {user.role == "JURY" && (
              <LikeButton
                onClick={handleLikeClick}
                isLiked={likedEnlargedEntries.includes(
                  entries?.content[enlargedPhotoIndex]
                )}
              />
            )}

            <Button
              onClick={
                descriptionOpen
                  ? () => setDescriptionOpen(false)
                  : () => setDescriptionOpen(true)
              }
            >
              {t("SinglesSection.descriptionTitle")}
            </Button>
          </div>
          {descriptionOpen && (
            <p className="absolute top-[15%] h-[70%] p-3 bg-slate-50 w-full">
              {entries.content[enlargedPhotoIndex]?.collection.description}
            </p>
          )}
        </div>
      </Modal>
    </>
  );
}
