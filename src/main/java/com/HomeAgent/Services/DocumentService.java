package com.HomeAgent.Services;


import com.HomeAgent.Model.Documents;
import com.HomeAgent.Model.User;

import com.HomeAgent.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }



    public void AddDocument(Documents documents){

        documentRepository.save(documents);
    }

    public List<Documents> getallDocuments(){

        return documentRepository.findAll();
    }

    public void delete(int id){
        documentRepository.deleteById(id);
    }

    public List<Documents> getDocumentsByUser(User user) {
        return documentRepository.findByUser(user);
    }


    public Documents getById(int documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }


    public long countByUser(User user) {
        return documentRepository.countByUser(user);
    }

    public List<Documents> getRecentDocuments(User user, int limit) {
        return documentRepository
                .findTop5ByUserOrderByDocumentIdDesc(user);
    }


    public List<Documents> getRecentDocs(User user) {
        return documentRepository.findTop5ByUserOrderByDocumentIdDesc(user);
    }
    public List<Documents> getDocumentsByUserId(Long userId) {
        return documentRepository.findByUserUserId(userId);
    }


}
