package org.upsam.tecmov.yourphotos.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.upsam.tecmov.yourphotos.controller.form.PhotoForm;
import org.upsam.tecmov.yourphotos.controller.view.PhotoView;
import org.upsam.tecmov.yourphotos.domain.photo.Photo;
import org.upsam.tecmov.yourphotos.domain.photo.PhotoRepository;
import org.upsam.tecmov.yourphotos.service.PhotoService;

@Service
public class PhotoServiceImpl implements PhotoService {

	/**
	 * Repositorio de fotos
	 */
	private PhotoRepository photoRepository;

	/**
	 * @param photoRepository
	 *            the photoRepository to set
	 */
	@Autowired
	public void setPhotoRepository(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	@Override
	@Transactional
	public boolean saveImage(PhotoForm form) throws IOException {
		byte[] bytes = null;
		boolean insert = false;
		if (form != null && !form.getPhoto().isEmpty()) {
			bytes = form.getPhoto().getBytes();
			Photo photo = new Photo();
			photo.setComment(form.getComment());
			photo.setPhoto(bytes);
			photo.setDate(new Date());
			photo.setContentType(form.getPhoto().getContentType());
			photoRepository.save(photo);
			insert = true;

		}
		return insert;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PhotoView> listAll() {
		Iterable<Photo> iterable = photoRepository.findAll();
		List<PhotoView> view = new ArrayList<PhotoView>();
		PhotoView pview = null;
		for (Photo photo : iterable) {
			pview = new PhotoView();
			pview.setId(photo.getId());
			pview.setComment(photo.getComment());
			pview.setDate(photo.getDate());
			pview.setContentType(photo.getContentType());
			view.add(pview);
		}
		return view;
	}

	@Override
	@Transactional(readOnly = true)
	public PhotoView getImage(Long id) {
		Photo photo = photoRepository.findOne(id);
		PhotoView pview = new PhotoView();
		pview.setPhoto(new ByteArrayInputStream(photo.getPhoto()));
		pview.setContentType(photo.getContentType());
		return pview;
	}

}