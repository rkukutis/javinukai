import formatTimestap from "../../utils/formatTimestap";
import { useState } from "react";
import { Photo } from "../photo/Photo";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import deleteEntry from "../../services/entries/deleteEntry";
import Button from "../Button";
import EnlargedPhotoCarousel from "../photo/EnlargedPhotoCarousel";
import deleteIcon from "../../assets/icons/delete_FILL0_wght400_GRAD0_opsz24.svg";
import detailsIcon from "../../assets/icons/description_FILL0_wght400_GRAD0_opsz24.svg";
import { useTranslation } from "react-i18next";

export default function ContestEntry({ entry, index, categoryType }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [fullScreenPhoto, setFullScreenPhoto] = useState(null);
  const { t } = useTranslation();

  const queryClient = useQueryClient();
  const { mutate, isPending } = useMutation({
    mutationFn: (entryId) => deleteEntry(entryId),
    onSuccess: () => {
      toast.success("Entry deleted successfuly");
      queryClient.invalidateQueries(["contestRecord"]);
    },
    onError: (err) => toast.error(err.message),
  });

  function handleDeleteEntry() {
    if (!confirm("Are you sure you want to delete this contest entry?")) return;
    setIsExpanded(false);
    mutate(entry.id);
  }

  return (
    <div className="py-2 px-3 rounded-md bg-white shadow">
      <div className="flex justify-between items-center">
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
        <div className="flex flex-col space-y-1 xl:space-y-0 xl:flex-row space-x-0 xl:space-x-3">
          <Button onClick={() => setIsExpanded(!isExpanded)}>
            <img src={detailsIcon} />
          </Button>
          <Button
            extraStyle="bg-red-500 hover:bg-red-400"
            onClick={handleDeleteEntry}
          >
            <img src={deleteIcon} />
          </Button>
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
