package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.SiteUser;
import com.meohin.todo_pe.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // 특정 사용자에게 속한 모든 Task 엔티티 목록을 반환한다.
    List<Task> findTasksBySiteUser(SiteUser user);

    // 주어진 subject를 포함하고 특정 사용자에게 속한 Task 엔티티 목록을 반환한다.
    List<Task> findTasksBySubjectContainsAndSiteUser(String subject, SiteUser user);

    // 특정 사용자에게 속한 Task 엔티티를 시작시각을 기준으로 내림차순으로 정렬하여 중복을 제외하고 반환한다.
    List<Task> findDistinctBySiteUserOrderByTaskMeasuresListStartTimeDesc(SiteUser user);
}
