import { useMutation, useQueryClient } from "@tanstack/react-query";
import LikeButton from "../LikeButton";
import addLike from "../../services/likes/addLike";
import removeLike from "../../services/likes/removeLike";

export default function SinglePhoto({ entry, onClick, key }) {
  const queryClient = useQueryClient();
  const { mutate: addLikeMutation } = useMutation({
    mutationFn: (entryId) => addLike(entryId),
    onSuccess: () => queryClient.invalidateQueries(["contestCategoryEntries"]),
  });
  const { mutate: removeLikeMutation } = useMutation({
    mutationFn: (entryId) => removeLike(entryId),
    onSuccess: () => queryClient.invalidateQueries(["contestCategoryEntries"]),
  });

  function handleLikeClick() {
    if (entry.liked) {
      removeLikeMutation(entry.collection.id);
    } else {
      addLikeMutation(entry.collection.id);
    }
  }

  return (
    <div key={key} className="relative">
      <img
        onClick={onClick}
        className="rounded"
        src={entry.collection.images[0].urlSmall}
      />
      <LikeButton
        onClick={handleLikeClick}
        extraStyle="absolute bottom-1 right-1"
        isLiked={entry.liked}
      />
    </div>
  );
}
