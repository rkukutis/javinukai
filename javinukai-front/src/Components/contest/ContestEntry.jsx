import { useState } from "react";
import Photo from "../photo/Photo";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import deleteEntry from "../../services/entries/deleteEntry";
import Button from "../Button";
import EnlargedPhotoCarousel from "../photo/EnlargedPhotoCarousel";
import deleteIcon from "../../assets/icons/delete_FILL0_wght400_GRAD0_opsz24.svg";
import detailsIcon from "../../assets/icons/description_FILL0_wght400_GRAD0_opsz24.svg";
import { useTranslation } from "react-i18next";
import Modal from "../Modal";
import updateEntry from "../../services/entries/updateEntry";
import { useForm } from "react-hook-form";
import FormFieldError from "../FormFieldError";
import StyledInput from "../StyledInput";

function EditEntrySection({ onClose, entry }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      newName: entry.name,
      newDescription: entry.description,
    },
  });
  const { t } = useTranslation();

  const queryClient = useQueryClient();
  const { mutate } = useMutation({
    mutationFn: (newData) => updateEntry(newData),
    onSuccess: () => {
      toast.success(t("EditEntrySection.editSuccess"));
      onClose();
      queryClient.invalidateQueries(["contestCategories"]);
    },
    onError: () => toast.error(t("EditEntrySection.editError")),
  });

  function onSubmit(newData) {
    mutate({ entryId: entry.id, newData });
  }

  return (
    <div className="flex flex-col space-y-3 p-6 h-fit md:w-[40vw]">
      <Button onClick={onClose} extraStyle="bg-red-500 hover:bg-red-400">
        {t("EditEntrySection.cancel")}
      </Button>
      <form className="w-full" onSubmit={handleSubmit(onSubmit)}>
        <div>
          <section className="form-field">
            <label>{t("EditEntrySection.newName")}</label>
            <input
              id="newName"
              className="form-field__input"
              {...register("newName", {
                maxLength: {
                  value: 100,
                  message: t("imageUpload.titleLength"),
                },
              })}
            />
            {errors.newName && (
              <FormFieldError>{errors.newName.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>{t("EditEntrySection.newDescription")}</label>
            <textarea
              id="newDescription"
              className="h-80 border-2 rounded p-3 my-2"
              {...register("newDescription", {
                required: t("imageUpload.photoDescriptionRequired"),
                maxLength: {
                  value: 1000,
                  message: t("imageUpload.descriptionLength"),
                },
              })}
            />
            {errors.newDescription && (
              <FormFieldError>{errors.newDescription.message}</FormFieldError>
            )}
          </section>
          <StyledInput
            value={t("EditEntrySection.submitEdit")}
            extraStyle="rounded p-3 py-2 w-full"
            type="submit"
            id="submit-entry-edit"
          />
        </div>
      </form>
    </div>
  );
}

export default function ContestEntry({ entry, index, categoryType }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [fullScreenPhoto, setFullScreenPhoto] = useState(null);
  const [editModalOpen, setEditModalOpen] = useState(false);
  const { t } = useTranslation();

  const queryClient = useQueryClient();
  const { mutate } = useMutation({
    mutationFn: (entryId) => deleteEntry(entryId),
    onSuccess: () => {
      toast.success(t("ContestEntry.deletionSuccess"));
      queryClient.invalidateQueries(["contestRecord"]);
    },
    onError: (err) => toast.error(err.message),
  });

  function handleDeleteEntry() {
    if (!confirm(t("ContestEntry.deletionConfirmation"))) return;
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
          <Button onClick={() => setEditModalOpen(true)}>
            {t("ContestEntry.editDetailsButton")}
          </Button>
          <Modal isOpen={editModalOpen} setIsOpen={setEditModalOpen}>
            <EditEntrySection
              entry={entry}
              onClose={() => setEditModalOpen(false)}
            />
          </Modal>
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
