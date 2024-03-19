import { useMutation, useQueryClient } from "@tanstack/react-query";
import LikeButton from "../LikeButton";
import addLike from "../../services/likes/addLike";
import removeLike from "../../services/likes/removeLike";

export default function SeriesPhotoCollection({ photo, onClick }) {
  console.log(photo);
  return (
    <img
      onClick={onClick}
      className="rounded hover:scale-105 hover:cursor-pointer"
      key={photo.id}
      src={photo.urlSmall}
    />
  );
}
