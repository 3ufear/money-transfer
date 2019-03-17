package ru.bank.service;

import io.micronaut.http.HttpStatus;
import lombok.extern.java.Log;
import ru.bank.exception.BusinessException;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Log
@Singleton
public class AccountLockService {

    private final ConcurrentMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public void acquire(final String lockId) {
        final ReentrantLock lock = locks.computeIfAbsent(lockId, key -> new ReentrantLock(true));
        acquireLock(lockId, lock);
    }

    public void release(final String lockId) {
        Optional.ofNullable(locks.get(lockId))
                .ifPresent(lock -> lock.unlock());
    }

    private void acquireLock(final String lockId, final ReentrantLock lock) {
        try {
            //ToDo: add config parameter
            if (!lock.tryLock(10, TimeUnit.SECONDS)) {
                throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't acquire lock for key:" + lockId);
            }
        } catch (InterruptedException ex) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "Lock not acquired");
        }
    }

}
