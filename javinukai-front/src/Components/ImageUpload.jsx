import { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import uploadImages from "../services/uploadImages";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { useForm } from "react-hook-form";
import StyledInput from "./StyledInput";

function getMultipartFormData(title, description, images) {
  let formData = new FormData();
  formData.append("categoryId", "a201c592-7daf-465e-92ab-6c3f9a45c1a9");
  formData.append("title", title);
  formData.append("description", description);
  images.map((image) => formData.append("image", image));
  return formData;
}

function ImageUpload() {
  const [files, setFiles] = useState([]);
  const [uploadedImages, setUploadedImages] = useState([]);
  const { register, reset, handleSubmit } = useForm();

  const { mutate, isPending } = useMutation({
    mutationFn: (multipartFormData) => uploadImages(multipartFormData),
    onSuccess: (createdCollection) => {
      toast.success("Images have been uploaded");
      const { images } = createdCollection;
      setUploadedImages(images);
    },
    onError: (err) => toast.error(err.message),
  });

  const onDrop = useCallback((acceptedFiles) => {
    setFiles(acceptedFiles);
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  function onSubmit(formData) {
    const { title, description } = formData;
    if (files.length === 0) {
      toast.error("You must add some images");
      return;
    }
    mutate(getMultipartFormData(title, description, files));
  }

  return (
    <div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <section className="form-field">
          <label>Title</label>
          <input
            className="form-field__input"
            {...register("title", {
              required: "Title is required",
            })}
          />
        </section>
        <section className="form-field">
          <label>Description</label>
          <textarea
            className="form-field__input"
            {...register("description", {
              required: "Description is required",
            })}
          />
        </section>
        <section>
          <div
            {...getRootProps()}
            className="text bg-slate-400 h-12 border-2 border-slate-800 hover:border-dashed"
          >
            <input {...getInputProps()} />
            {isDragActive ? (
              <p>Drop the files here ...</p>
            ) : (
              <p>Drag 'n' drop some files here, or click to select files</p>
            )}
          </div>
        </section>
        <StyledInput
          disabled={isPending}
          id="image-upload-form"
          type="submit"
          value={isPending ? "Uploading..." : "Upload image(s)"}
        />
      </form>
      {uploadedImages.map((image) => (
        <img key={image.uuid} src={image.urlThumbnail} />
      ))}
    </div>
  );
}
export default ImageUpload;
