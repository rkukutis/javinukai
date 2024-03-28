import { useMutation, useQueryClient } from "@tanstack/react-query";
import LikeButton from "../LikeButton";
import addLike from "../../services/likes/addLike";
import removeLike from "../../services/likes/removeLike";
import useUserStore from "../../stores/userStore";
import { useTranslation } from "react-i18next";

export default function SinglePhotoCollection({ entry, onClick }) {
  const queryClient = useQueryClient();
  const { t } = useTranslation();
  const { mutate: addLikeMutation } = useMutation({
    mutationFn: (entryId) => addLike(entryId),
    onSuccess: () => queryClient.invalidateQueries(["contestCategoryEntries"]),
  });
  const { mutate: removeLikeMutation } = useMutation({
    mutationFn: (entryId) => removeLike(entryId),
    onSuccess: () => queryClient.invalidateQueries(["contestCategoryEntries"]),
  });
  const { user } = useUserStore();

  function handleLikeClick() {
    if (entry.liked) {
      removeLikeMutation(entry.collection.id);
    } else {
      addLikeMutation(entry.collection.id);
    }
  }

  return (
    <div className="flex flex-col">
      {(user.role == "ADMIN" || user.role == "MODERATOR") && (
        <div className="py-2 bg-slate-50 px-2 mb-2 rounded">
          <p>
            {t("SinglePhotoCollection.author")}: {entry.collection.author.name}{" "}
            {entry.collection.author.surname}
          </p>
          <p>
            {t("SinglePhotoCollection.likes")}: {entry.collection.likesCount}
          </p>
        </div>
      )}

      <div className="relative">
        <img
          onClick={onClick}
          className="rounded"
          src={entry.collection.images[0].urlSmall}
        />
        {user.role == "JURY" && (
          <LikeButton
            onClick={handleLikeClick}
            extraStyle="absolute bottom-1 right-1"
            isLiked={entry.liked}
          />
        )}
      </div>
    </div>
  );
}
